import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildPollItem, defaultValue } from 'app/shared/model/bot/guild-poll-item.model';

export const ACTION_TYPES = {
  FETCH_GUILDPOLLITEM_LIST: 'guildPollItem/FETCH_GUILDPOLLITEM_LIST',
  FETCH_GUILDPOLLITEM: 'guildPollItem/FETCH_GUILDPOLLITEM',
  CREATE_GUILDPOLLITEM: 'guildPollItem/CREATE_GUILDPOLLITEM',
  UPDATE_GUILDPOLLITEM: 'guildPollItem/UPDATE_GUILDPOLLITEM',
  DELETE_GUILDPOLLITEM: 'guildPollItem/DELETE_GUILDPOLLITEM',
  RESET: 'guildPollItem/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildPollItem>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildPollItemState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildPollItemState = initialState, action): GuildPollItemState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDPOLLITEM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDPOLLITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDPOLLITEM):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDPOLLITEM):
    case REQUEST(ACTION_TYPES.DELETE_GUILDPOLLITEM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDPOLLITEM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDPOLLITEM):
    case FAILURE(ACTION_TYPES.CREATE_GUILDPOLLITEM):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDPOLLITEM):
    case FAILURE(ACTION_TYPES.DELETE_GUILDPOLLITEM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDPOLLITEM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDPOLLITEM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDPOLLITEM):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDPOLLITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDPOLLITEM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/guild-poll-items';

// Actions

export const getEntities: ICrudGetAllAction<IGuildPollItem> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDPOLLITEM_LIST,
  payload: axios.get<IGuildPollItem>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildPollItem> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDPOLLITEM,
    payload: axios.get<IGuildPollItem>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildPollItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDPOLLITEM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildPollItem> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDPOLLITEM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildPollItem> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDPOLLITEM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
