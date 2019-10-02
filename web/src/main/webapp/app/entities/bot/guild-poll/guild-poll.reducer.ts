import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGuildPoll, defaultValue } from 'app/shared/model/bot/guild-poll.model';

export const ACTION_TYPES = {
  FETCH_GUILDPOLL_LIST: 'guildPoll/FETCH_GUILDPOLL_LIST',
  FETCH_GUILDPOLL: 'guildPoll/FETCH_GUILDPOLL',
  CREATE_GUILDPOLL: 'guildPoll/CREATE_GUILDPOLL',
  UPDATE_GUILDPOLL: 'guildPoll/UPDATE_GUILDPOLL',
  DELETE_GUILDPOLL: 'guildPoll/DELETE_GUILDPOLL',
  RESET: 'guildPoll/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildPoll>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildPollState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildPollState = initialState, action): GuildPollState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDPOLL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDPOLL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDPOLL):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDPOLL):
    case REQUEST(ACTION_TYPES.DELETE_GUILDPOLL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDPOLL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDPOLL):
    case FAILURE(ACTION_TYPES.CREATE_GUILDPOLL):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDPOLL):
    case FAILURE(ACTION_TYPES.DELETE_GUILDPOLL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDPOLL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDPOLL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDPOLL):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDPOLL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDPOLL):
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

const apiUrl = 'services/bot/api/guild-polls';

// Actions

export const getEntities: ICrudGetAllAction<IGuildPoll> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDPOLL_LIST,
  payload: axios.get<IGuildPoll>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildPoll> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDPOLL,
    payload: axios.get<IGuildPoll>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildPoll> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDPOLL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildPoll> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDPOLL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildPoll> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDPOLL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
