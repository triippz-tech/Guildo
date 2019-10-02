import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IScheduledAnnouncement, defaultValue } from 'app/shared/model/bot/scheduled-announcement.model';

export const ACTION_TYPES = {
  FETCH_SCHEDULEDANNOUNCEMENT_LIST: 'scheduledAnnouncement/FETCH_SCHEDULEDANNOUNCEMENT_LIST',
  FETCH_SCHEDULEDANNOUNCEMENT: 'scheduledAnnouncement/FETCH_SCHEDULEDANNOUNCEMENT',
  CREATE_SCHEDULEDANNOUNCEMENT: 'scheduledAnnouncement/CREATE_SCHEDULEDANNOUNCEMENT',
  UPDATE_SCHEDULEDANNOUNCEMENT: 'scheduledAnnouncement/UPDATE_SCHEDULEDANNOUNCEMENT',
  DELETE_SCHEDULEDANNOUNCEMENT: 'scheduledAnnouncement/DELETE_SCHEDULEDANNOUNCEMENT',
  SET_BLOB: 'scheduledAnnouncement/SET_BLOB',
  RESET: 'scheduledAnnouncement/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IScheduledAnnouncement>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ScheduledAnnouncementState = Readonly<typeof initialState>;

// Reducer

export default (state: ScheduledAnnouncementState = initialState, action): ScheduledAnnouncementState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULEDANNOUNCEMENT):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULEDANNOUNCEMENT):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULEDANNOUNCEMENT):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULEDANNOUNCEMENT):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULEDANNOUNCEMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULEDANNOUNCEMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/scheduled-announcements';

// Actions

export const getEntities: ICrudGetAllAction<IScheduledAnnouncement> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT_LIST,
  payload: axios.get<IScheduledAnnouncement>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IScheduledAnnouncement> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULEDANNOUNCEMENT,
    payload: axios.get<IScheduledAnnouncement>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IScheduledAnnouncement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULEDANNOUNCEMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IScheduledAnnouncement> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULEDANNOUNCEMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IScheduledAnnouncement> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULEDANNOUNCEMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
